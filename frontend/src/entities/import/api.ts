import { z } from "zod";
import {
  PaginatedQuerySchema,
  PaginatedResponseSchema,
} from "@/shared/pagination.ts";
import { queryOptions } from "@tanstack/react-query";
import { ApiInstance } from "@/shared/instance.ts";

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
  finishedAt: z
    .null()
    .nullish()
    .transform((it) => it ?? undefined),
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
        values: data.values
      })
    },
  });
};
