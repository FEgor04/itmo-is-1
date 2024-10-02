import { getHumansQueryOptions } from "@/entities/human-being/api";
import { useHumanBeingTable } from "@/entities/human-being/table";
import { PaginatedQuerySchema } from "@/shared/pagination";
import { DataTable } from "@/shared/ui/data-table";
import { createFileRoute } from "@tanstack/react-router";

const SearchSchema = PaginatedQuerySchema;

export const Route = createFileRoute("/humans")({
  component: Page,
  validateSearch: SearchSchema,
  loaderDeps: ({ search }) => search,
  loader: ({ context, deps }) => {
    return context.queryClient.ensureQueryData(getHumansQueryOptions(deps));
  },
});

function Page() {
  const data = Route.useLoaderData();
  const table = useHumanBeingTable(data.values);
  return <DataTable table={table} />;
}
