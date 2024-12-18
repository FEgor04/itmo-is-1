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
import { toast } from "sonner";

export const GetHumansQuerySchema = PaginatedQuerySchema.merge(
  SortingQuerySchema(FetchedHumanBeingSchemaKeys),
).extend({
  name: z.string().min(1).optional().catch(undefined),
  impactSpeedLowerThan: z.number().optional().catch(undefined),
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
      const { data } = await ApiInstance.api.getAllHumans({
        page: validated.page,
        pageSize: validated.pageSize,
        // @ts-expect-error fix after https://github.com/FEgor04/itmo-is-1/issues/23
        sortBy: (validated.sortBy ?? "id").replace("_", "."),
        sortDirection: validated.sortDirection ?? "asc",
        name: validated.name,
        impactSpeedLT: validated.impactSpeedLowerThan,
      });
      return GetHumansResponseSchema.parse({
        values: data.values.map(parseHumanBeingDTO),
        total: data.total,
        page: data.page,
        pageSize: data.pageSize,
      });
    },
  });
};

export const CreateHumanBeingSchema = BaseHumanBeingSchema.omit({
  id: true,
  creationDate: true,
  ownerId: true,
}).extend({
  car: CarIDSchema,
});

export function useCreateHumanBeingMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (valuesRaw: z.infer<typeof CreateHumanBeingSchema>) => {
      const values = CreateHumanBeingSchema.parse(valuesRaw);
      const { data } = await ApiInstance.api.createHuman({
        name: values.name,
        carId: values.car,
        x: values.coordinates.x,
        y: values.coordinates.y,
        mood: values.mood,
        realHero: values.realHero,
        hasToothpick: values.hasToothpick,
        impactSpeed: values.impactSpeed,
        weaponType: values.weaponType,
      });
      return parseHumanBeingDTO(data);
    },
    onSuccess: () => {
      return queryClient.invalidateQueries({ queryKey: ["humans"] });
    },
  });
}

export function useDeleteHumanBeingMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (id: number) => {
      toast.loading(`Удаляем человека`, { id: `delete-human-${id}` });
      await ApiInstance.api.deleteHuman(id);
      return;
    },
    onSuccess: (_, id) => {
      toast.success(`Человек удален!`, { id: `delete-human-${id}` });
      return queryClient.invalidateQueries({ queryKey: ["humans"] });
    },
    onError: (err, id) => {
      toast.error(`Не удалось удалить человека. Ошибка: ${err}`, {
        id: `delete-human-${id}`,
      });
    },
  });
}

export const EditHumanBeingSchema = BaseHumanBeingSchema.omit({
  creationDate: true,
  ownerId: true,
}).extend({
  car: CarIDSchema,
});

export function useEditHumanBeingMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (valuesRaw: z.infer<typeof EditHumanBeingSchema>) => {
      const values = EditHumanBeingSchema.parse(valuesRaw);
      const { data } = await ApiInstance.api.updateHuman(values.id, {
        name: values.name,
        carId: values.car,
        x: values.coordinates.x,
        y: values.coordinates.y,
        mood: values.mood,
        realHero: values.realHero,
        hasToothpick: values.hasToothpick,
        impactSpeed: values.impactSpeed,
        weaponType: values.weaponType,
      });
      return parseHumanBeingDTO(data);
    },
    onSuccess: () => {
      return queryClient.invalidateQueries({ queryKey: ["humans"] });
    },
  });
}
