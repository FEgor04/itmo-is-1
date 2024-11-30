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
import { BaseCarSchema, CarSchemaKeys } from "./model";
import { SortingQuerySchema } from "@/shared/sorting";
import { ApiInstance } from "@/shared/instance";
import { toast } from "sonner";

export const GetCarsQuerySchema = PaginatedQuerySchema.merge(
  SortingQuerySchema(CarSchemaKeys),
).extend({
  brand: z.string().min(1).optional().catch(undefined),
  model: z.string().min(1).optional().catch(undefined),
  color: z.string().min(1).optional().catch(undefined),
});
export const GetCarsResponseSchema = PaginatedResponseSchema(BaseCarSchema);

export const getCarsQueryOptions = (
  query: z.infer<typeof GetCarsQuerySchema>,
) => {
  const validated = GetCarsQuerySchema.parse(query);
  return queryOptions({
    queryKey: ["cars", "list", validated],
    queryFn: async () => {
      const { data } = await ApiInstance.api.getAllCars({
        page: validated.page,
        pageSize: validated.pageSize,
        sortBy: validated.sortBy ?? "id",
        sortDirection: validated.sortDirection ?? "asc",
        model: validated.model,
        brand: validated.brand,
        color: validated.color,
      });
      return GetCarsResponseSchema.parse(data);
    },
  });
};

export const CreateCarSchema = z.object({
  brand: z.string(),
  model: z.string(),
  color: z.string(),
  cool: z.boolean(),
});

export function useCreateCarMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (valuesRaw: z.infer<typeof CreateCarSchema>) => {
      const values = CreateCarSchema.parse(valuesRaw);
      const { data } = await ApiInstance.api.createCar({
        brand: values.brand,
        model: values.model,
        color: values.color,
        cool: values.cool,
      });
      return data;
    },
    onSuccess: () => {
      return queryClient.invalidateQueries({ queryKey: ["cars"] });
    },
  });
}

export const UpdateCarSchema = CreateCarSchema.extend({
  id: z.number(),
});

export function useEditCarMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (valuesRaw: z.infer<typeof UpdateCarSchema>) => {
      const values = UpdateCarSchema.parse(valuesRaw);
      const { data } = await ApiInstance.api.updateCar(values.id, {
        brand: values.brand,
        model: values.model,
        color: values.color,
        cool: values.cool,
      });
      return data;
    },
    onSuccess: () => {
      return queryClient.invalidateQueries({ queryKey: ["cars"] });
    },
  });
}

export function useDeleteCarMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (id: number) => {
      toast.loading(`Удаляем машину`, { id: `delete-car-${id}` });
      const { data } = await ApiInstance.api.deleteCar(id);
      return data;
    },
    onSuccess: (_, id) => {
      toast.success(`Машина удалена!`, { id: `delete-car-${id}` });
      return queryClient.invalidateQueries({ queryKey: ["cars"] });
    },
    onError: (err, id) => {
      toast.error(`Не удалось удалить машину. Ошибка: ${err}`, {
        id: `delete-car-${id}`,
      });
    },
  });
}
