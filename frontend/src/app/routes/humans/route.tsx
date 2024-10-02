import { getHumansQueryOptions } from "@/entities/human-being/api";
import { useHumanBeingTable } from "@/entities/human-being/table";
import { PaginatedQuerySchema } from "@/shared/pagination";
import { DataTable } from "@/shared/ui/data-table";
import { PaginationFooter } from "@/shared/ui/pagination";
import { createFileRoute } from "@tanstack/react-router";
import { z } from "zod";

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
  const query = Route.useSearch();
  const navigate = Route.useNavigate();
  const data = Route.useLoaderData();
  const table = useHumanBeingTable(data.values);

  function setQuery(
    updater: (
      previous: z.infer<typeof SearchSchema>,
    ) => z.infer<typeof SearchSchema>,
  ) {
    void navigate({
      search: updater,
    });
  }

  return (
    <div className="space-y-4">
      <main>
        <DataTable table={table} />
      </main>
      <PaginationFooter query={query} setQuery={setQuery} total={data.total} />
    </div>
  );
}
