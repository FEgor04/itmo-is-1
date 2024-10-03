import {
  PaginatedQuerySchema,
  PaginatedResponseSchema,
} from "@/shared/pagination";
import { queryOptions } from "@tanstack/react-query";
import { z } from "zod";
import { BaseCarSchema, CarSchemaKeys } from "./model";
import { SortingQuerySchema } from "@/shared/sorting";

export const GetCarsQuerySchema = PaginatedQuerySchema.merge(
  SortingQuerySchema(CarSchemaKeys),
).extend({
  brand: z.string().min(1).optional().catch(undefined),
  model: z.string().min(1).optional().catch(undefined),
});
export const GetCarsResponseSchema = PaginatedResponseSchema(BaseCarSchema);

export const getCarsQueryOptions = (
  query: z.infer<typeof GetCarsQuerySchema>,
) => {
  const validated = GetCarsQuerySchema.parse(query);
  return queryOptions({
    queryKey: ["cars", "list", validated],
    queryFn: async () => {
      await new Promise((res) => setInterval(res, 500));
      const values = [
        {
          id: 1,
          brand: "Lada",
          model: "Kalina",
          color: "red",
          cool: true,
        },
      ];
      return GetCarsResponseSchema.parse({
        values,
        total: 500,
        page: query.page,
        pageSize: query.pageSize,
      });
    },
  });
};
