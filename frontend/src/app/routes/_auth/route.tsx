import { getPrincipalQueryOptions } from "@/entities/principal/api";
import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth")({
  component: Outlet,
  beforeLoad: async ({ context }) => {
    try {
      await context.queryClient.ensureQueryData(getPrincipalQueryOptions());
    } catch (e) {
      throw redirect({
        to: "/signin",
      });
    }
  },
});
