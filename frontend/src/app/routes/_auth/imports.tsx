import { createFileRoute } from "@tanstack/react-router";
import {
  getImportsQueryOptions,
  getImportsRequestSchema,
  useImportsTable,
} from "@/entities/import";
import { useQuery } from "@tanstack/react-query";
import { CrudControlsHeader } from "@/shared/ui/controls.tsx";
import { DataTable } from "@/shared/ui/data-table.tsx";
import { PaginationFooter } from "@/shared/ui/pagination.tsx";

export const Route = createFileRoute("/_auth/imports")({
  validateSearch: getImportsRequestSchema,
  loaderDeps: ({ search }) => search,
  loader: ({ deps, context }) => {
    return context.queryClient.ensureQueryData(getImportsQueryOptions(deps));
  },
  component: Page,
});

function Page() {
  const query = Route.useSearch();
  const navigate = Route.useNavigate();
  const initialData = Route.useLoaderData();
  const { data } = useQuery({ ...getImportsQueryOptions(query), initialData });
  const values = data.values;

  const table = useImportsTable(values);

  return (
    <div className="space-y-4">
      <CrudControlsHeader>
        <div></div>
      </CrudControlsHeader>
      <main>
        <DataTable table={table} />
      </main>
      <PaginationFooter
        query={query}
        setQuery={(query) => navigate({ search: query })}
        total={data.total}
      />
    </div>
  );
}
