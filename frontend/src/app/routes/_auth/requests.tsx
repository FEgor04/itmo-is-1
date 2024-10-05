import { PaginatedQuerySchema } from "@/shared/pagination";
import { createFileRoute } from "@tanstack/react-router";

const SearchSchema = PaginatedQuerySchema;

export const Route = createFileRoute("/_auth/requests")({
  component: () => <div>Hello /_auth/requests!</div>,
  validateSearch: SearchSchema,
});
