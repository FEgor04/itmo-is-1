import {
  PaginatedQuerySchema,
  PaginatedResponseSchema,
} from "@/shared/pagination";
import { queryOptions } from "@tanstack/react-query";
import { z } from "zod";
import { BaseCarSchema, CarSchemaKeys } from "./model";
import { SortingQuerySchema } from "@/shared/sorting";
import { ApiInstance } from "@/shared/instance";

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
      const { data } = await ApiInstance.cars.getAllCars({
        page: validated.page,
        pageSize: validated.pageSize,
        sortBy: validated.sortBy ?? "id",
        sortDirection: validated.sortDirection ?? "asc",
        model: validated.model,
        brand: validated.brand,
      });
      return GetCarsResponseSchema.parse(data);
    },
  });
};
