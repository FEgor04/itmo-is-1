import { ApiInstance } from "@/shared/instance";
import {
  queryOptions,
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import { z } from "zod";

const GetMeResponseSchema = z.object({
  username: z.string(),
  role: z.enum(["USER", "ADMIN"]),
  adminRequestStatus: z.enum(["PENDING", "APPROVED", "REJECTED", "NO_REQUEST"]),
});
export const getPrincipalQueryOptions = () =>
  queryOptions({
    queryKey: ["principal"],
    queryFn: async () => {
      const { data } = await ApiInstance.api.me();
      return GetMeResponseSchema.parse(data);
    },
  });

export function useSendAdminRequestMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async () => {
      const { data } = await ApiInstance.api.submitAdminRequest();
      return data;
    },
    onSuccess: () => {
      return queryClient.invalidateQueries({ queryKey: ["principal"] });
    },
  });
}
