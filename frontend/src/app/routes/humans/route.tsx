import {
  getHumansQueryOptions,
  GetHumansQuerySchema,
} from "@/entities/human-being/api";
import {
  FetchedHumanBeingSchemaKeys,
} from "@/entities/human-being/model";
import { useHumanBeingTable } from "@/entities/human-being/table";
import { DataTable } from "@/shared/ui/data-table";
import { PaginationFooter } from "@/shared/ui/pagination";
import { createFileRoute } from "@tanstack/react-router";
import { SortingState } from "@tanstack/react-table";
import { z } from "zod";

const SearchSchema = GetHumansQuerySchema;

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
  const sortingState: SortingState =
    query.sortDirection && query.sortBy
      ? [
          {
            desc: query.sortDirection == "desc",
            id: query.sortBy,
          },
        ]
      : [];

  function setQuery(
    updater: (
      previous: z.infer<typeof SearchSchema>,
    ) => z.infer<typeof SearchSchema>,
  ) {
    void navigate({
      search: updater,
    });
  }

  const table = useHumanBeingTable(
    data.values,
    sortingState,
    (updaterOrValue) => {
      if (typeof updaterOrValue == "function") {
        const newSorting = updaterOrValue(sortingState);
        if (newSorting.length > 0) {
          setQuery((prev) => ({
            ...prev,
            sortBy: newSorting[0].id as z.infer<
              typeof FetchedHumanBeingSchemaKeys
            >,
            sortDirection: newSorting[0].desc ? "desc" : "asc",
          }));
        } else {
          setQuery((prev) => ({
            ...prev,
            sortBy: undefined,
            sortDirection: undefined,
          }));
        }
      }
    },
  );

  return (
    <div className="space-y-4">
      <main>
        <DataTable table={table} />
      </main>
      <PaginationFooter query={query} setQuery={setQuery} total={data.total} />
    </div>
  );
}
