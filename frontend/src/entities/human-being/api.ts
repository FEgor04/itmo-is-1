import {
  PaginatedQuerySchema,
  PaginatedResponseSchema,
} from "@/shared/pagination";
import { queryOptions } from "@tanstack/react-query";
import { z } from "zod";
import { FetchedHumanBeingSchema, FetchedHumanBeingSchemaKeys } from "./model";
import { SortingQuerySchema } from "@/shared/sorting";

export const GetHumansQuerySchema = PaginatedQuerySchema.merge(
  SortingQuerySchema(FetchedHumanBeingSchemaKeys),
);
export const GetHumansResponseSchema = PaginatedResponseSchema(
  FetchedHumanBeingSchema,
);

export const getHumansQueryOptions = (
  query: z.infer<typeof GetHumansQuerySchema>,
) => {
  const validated = GetHumansQuerySchema.parse(query);
  return queryOptions({
    queryKey: ["humans", "list", validated],
    queryFn: async () => {
      const values = [
        {
          id: 1,
          name: "asd",
          realHero: true,
          mood: "CALM",
          coordinates: {
            x: 1,
            y: 1,
          },
          creationDate: new Date(),
          hasToothpick: true,
          weaponType: "AXE",
          car: {
            cool: true,
          },
          impactSpeed: 50,
        },
      ];
      return GetHumansResponseSchema.parse({
        values,
        total: 500,
        page: query.page,
        pageSize: query.pageSize,
      });
    },
  });
};
