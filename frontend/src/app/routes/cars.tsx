import { getCarsQueryOptions, GetCarsQuerySchema } from "@/entities/car/api";
import { CarKeys } from "@/entities/car/model";
import { useCarTable } from "@/entities/car/table";
import { Button } from "@/shared/ui/button";
import { DataTable } from "@/shared/ui/data-table";
import { Input } from "@/shared/ui/input";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { SortingState } from "@tanstack/react-table";
import { PlusCircle, SearchIcon } from "lucide-react";
import { useDebouncedCallback } from "use-debounce";
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

  const debouncedSetBrand = useDebouncedCallback((brand: string) => {
    setQuery((prev) => ({
      ...prev,
      brand,
    }));
  }, 200);

  const debouncedSetModel = useDebouncedCallback((model: string) => {
    setQuery((prev) => ({
      ...prev,
      model,
    }));
  }, 200);

  return (
    <div className="space-y-4">
      <header className="flex">
        <Button variant="outline">
          <PlusCircle className="mr-2 size-4" />
          Создать
        </Button>
        <div className="ml-auto flex items-center space-x-2">
          <div className="flex">
            <span className="inline-flex h-8 items-center rounded-md rounded-r-none border border-r-0 border-input px-2 align-middle text-sm">
              <SearchIcon className="mr-2 size-4" />
              Брэнд
            </span>
            <Input
              className="h-8 rounded-l-none ring-0"
              defaultValue={search.brand}
              onChange={(e) => debouncedSetBrand(e.target.value)}
            />
          </div>
          <div className="flex">
            <span className="inline-flex h-8 items-center rounded-md rounded-r-none border border-r-0 border-input px-2 align-middle text-sm">
              <SearchIcon className="mr-2 size-4" />
              Модель
            </span>
            <Input
              className="h-8 rounded-l-none ring-0"
              defaultValue={search.model}
              onChange={(e) => debouncedSetModel(e.target.value)}
            />
          </div>
        </div>
      </header>
      <main>
        <DataTable table={table} />
      </main>
    </div>
  );
}
