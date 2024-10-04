import { getCarsQueryOptions, GetCarsQuerySchema } from "@/entities/car/api";
import { CarKeys } from "@/entities/car/model";
import { useCarTable } from "@/entities/car/table";
import { DataTable } from "@/shared/ui/data-table";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { SortingState } from "@tanstack/react-table";
import { z } from "zod";

const SearchSchema = GetCarsQuerySchema;
export const Route = createFileRoute("/cars")({
  component: Page,
  validateSearch: SearchSchema,
  loaderDeps: ({ search }) => search,
  loader: ({ context, deps }) => {
    return context.queryClient.ensureQueryData(getCarsQueryOptions(deps));
  },
});

function Page() {
  const search = Route.useSearch();
  const initialData = Route.useLoaderData();
  const { data } = useQuery({
    ...getCarsQueryOptions(search),
    initialData,
  });
  const navigate = Route.useNavigate();
  function setQuery(
    updater: (
      prev: z.infer<typeof SearchSchema>,
    ) => z.infer<typeof SearchSchema>,
  ) {
    navigate({
      search: (prev) => updater(prev),
    });
  }
  const sortingState = [
    {
      id: search.sortBy ?? "id",
      desc: search.sortDirection == "desc",
    },
  ];
  const table = useCarTable(data.values, sortingState, (updaterOrValue) => {
    let newValue: SortingState;
    if (typeof updaterOrValue == "function") {
      newValue = updaterOrValue(sortingState);
    } else {
      newValue = updaterOrValue;
    }
    setQuery((prev) => ({
      ...prev,
      sortBy: newValue[0].id as CarKeys,
      sortDirection: newValue[0].desc ? "desc" : "asc",
    }));
  });
  return (
    <div>
      <main>
        <DataTable table={table} />
      </main>
    </div>
  );
}
