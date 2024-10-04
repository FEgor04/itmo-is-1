import { ApiInstance } from "@/shared/instance";
import { queryOptions } from "@tanstack/react-query";

export const getPrincipalQueryOptions = () =>
  queryOptions({
    queryKey: ["principal"],
    queryFn: async () => {
      const { data } = await ApiInstance.api.me();
      return data;
    },
  });
