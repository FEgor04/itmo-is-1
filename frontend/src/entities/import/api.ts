import { z } from "zod";
import {
  PaginatedQuerySchema,
  PaginatedResponseSchema,
} from "@/shared/pagination.ts";
import { queryOptions } from "@tanstack/react-query";

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
      return getImportsResponseSchema.parse({
        page: 1,
        pageSize: request.pageSize,
        total: 50,
        values: [
          {
            id: 1,
            status: "error",
            message: "Error !!!!",
            author: {
              id: 55,
              username: "e.fedorov",
            },
            startedAt: new Date(),
            finishedAt: new Date(),
          },
          {
            id: 2,
            status: "inProgress",
            author: {
              id: 55,
              username: "e.fedorov",
            },
            startedAt: new Date(),
          },
          {
            id: 3,
            status: "finished",
            author: {
              id: 55,
              username: "e.fedorov",
            },
            startedAt: new Date(),
            finishedAt: new Date(),
            createdEntities: 500,
          },
        ],
      } satisfies z.infer<typeof getImportsResponseSchema>);
    },
  });
};
