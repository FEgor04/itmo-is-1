import { ApiInstance } from "@/shared/instance";
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
import { AdminRequestSchema } from "./requests-table";

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

const getAdminRequestsQuerySchema = PaginatedQuerySchema;
type getAdminRequests = z.infer<typeof getAdminRequestsQuerySchema>;
const GetAdminRequestsResponseSchema =
  PaginatedResponseSchema(AdminRequestSchema);
export const getAdminRequests = (query: getAdminRequests) => {
  const validated = getAdminRequestsQuerySchema.parse(query);
  return queryOptions({
    queryKey: ["admin-requests", query],
    queryFn: async () => {
      const { data } = await ApiInstance.api.getAllAdminRequests({
        page: validated.page,
        pageSize: validated.pageSize,
        sortBy: "username",
        sortDirection: "asc",
      });
      return GetAdminRequestsResponseSchema.parse(data);
    },
  });
};

export function useApproveAdminRequestMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (username: string) => {
      await ApiInstance.api.approveAdminRequest(username);
      return;
    },
    onSuccess: () => {
      return queryClient.invalidateQueries({ queryKey: ["admin-requests"] });
    },
  });
}

export function useRejectAdminRequestMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (username: string) => {
      await ApiInstance.api.rejectedAdminRequest(username);
      return;
    },
    onSuccess: () => {
      return queryClient.invalidateQueries({ queryKey: ["admin-requests"] });
    },
  });
}
