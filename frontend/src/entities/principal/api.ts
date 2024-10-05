import { ApiInstance } from "@/shared/instance";
import { queryOptions } from "@tanstack/react-query";
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
