import { z } from "zod";
import {
  PaginatedQuerySchema,
  PaginatedResponseSchema,
} from "@/shared/pagination.ts";
import { queryOptions, useMutation } from "@tanstack/react-query";
import { ApiInstance } from "@/shared/instance.ts";
import { ImportDto } from "@/shared/api.gen.ts";

const BaseImport = z.object({
  id: z.number(),
  author: z.object({
    id: z.number(),
    username: z.string(),
  }),
  startedAt: z.coerce.date(),
});

export const SuccessfulImport = BaseImport.extend({
  status: z.literal("finished"),
  createdEntities: z.number(),
  finishedAt: z.coerce.date(),
});

export const InProgressImport = BaseImport.extend({
  status: z.literal("inProgress"),
});

export const ErrorImport = BaseImport.extend({
  status: z.literal("error"),
  message: z.string(),
  finishedAt: z.coerce.date(),
});

const ImportSchema = z.discriminatedUnion("status", [
  SuccessfulImport,
  InProgressImport,
  ErrorImport,
]);

export const getImportsRequestSchema = PaginatedQuerySchema;

export const getImportsResponseSchema = PaginatedResponseSchema(ImportSchema);

export type Import = z.infer<typeof ImportSchema>;

function parseDTO(dto: ImportDto): Import {
  if(dto.status == "FINISHED") {
    return SuccessfulImport.parse({
      id: dto.id,
      status: "finished",
      finishedAt: dto.finishedAt + "Z",
      author: dto.author,

      startedAt: dto.createdAt + "Z",
      createdEntities: dto.createdEntitiesCount,
    })
  }

  if(dto.status == "FAILED") {
    return ErrorImport.parse({
      id: dto.id,
      status: "error",
      author: dto.author,
      startedAt: dto.createdAt + "Z",
      finishedAt: dto.finishedAt + "Z",
      message: dto.message
    })
  }

  return InProgressImport.parse({
    id: dto.id,
    status: "inProgress",
    author: dto.author,
    startedAt: dto.createdAt + "Z",
  })
}

export const getImportsQueryOptions = (
  requestRaw: z.infer<typeof getImportsRequestSchema>,
) => {
  const request = getImportsRequestSchema.parse(requestRaw);
  return queryOptions({
    queryKey: ["imports", "list", request],
    queryFn: async () => {
      const {data} = await ApiInstance.api.getAllImports({
        page: request.page,
        pageSize: request.pageSize,
      });

      return getImportsResponseSchema.parse({
        page: data.page,
        pageSize: data.pageSize,
        total: data.total,
        values: data.values.map(parseDTO)
      })
    },
  });
};

export function useUploadImportMutation() {
  return useMutation({
    mutationFn: async (file: File) => {
      const formData = new FormData();
      formData.append("file", file)
      const {data} = await ApiInstance.api.import(formData)

      return parseDTO(data);
    }
  })
}
