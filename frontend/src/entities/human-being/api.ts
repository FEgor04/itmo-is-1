import {
  PaginatedQuerySchema,
  PaginatedResponseSchema,
} from "@/shared/pagination";
import {
  queryOptions,
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import { z } from "zod";
import {
  BaseHumanBeingSchema,
  FetchedHumanBeingSchema,
  FetchedHumanBeingSchemaKeys,
  parseHumanBeingDTO,
} from "./model";
import { SortingQuerySchema } from "@/shared/sorting";
import { CarIDSchema } from "../car/model";
import { ApiInstance } from "@/shared/instance";

export const GetHumansQuerySchema = PaginatedQuerySchema.merge(
  SortingQuerySchema(FetchedHumanBeingSchemaKeys),
).extend({
  name: z.string().min(1).optional().catch(undefined),
});
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
            id: 1,
            brand: "Lada",
            model: "Kalina",
            color: "red",
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

export const CreateHumanBeingSchema = BaseHumanBeingSchema.omit({
  id: true,
  creationDate: true,
}).extend({
  car: CarIDSchema,
});

export function useCreateHumanBeingMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (valuesRaw: z.infer<typeof CreateHumanBeingSchema>) => {
      const values = CreateHumanBeingSchema.parse(valuesRaw);
      const { data } = await ApiInstance.humans.createHuman({
        name: values.name,
        carId: values.car,
        x: values.coordinates.x,
        y: values.coordinates.y,
        mood: values.mood,
        realHero: values.realHero,
        hasToothpick: values.hasToothpick,
        // TODO: remove ?? after https://github.com/FEgor04/itmo-is-1/issues/21
        impactSpeed: values.impactSpeed ?? -1,
        weaponType: values.weaponType,
      });
      return parseHumanBeingDTO(data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["humans"] });
    },
  });
}
