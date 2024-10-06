import { getCarsQueryOptions, GetCarsQuerySchema } from "@/entities/car/api";
import { CreateCarDialogContent } from "@/entities/car/create";
import { CarKeys } from "@/entities/car/model";
import { useCarTable } from "@/entities/car/table";
import { Button } from "@/shared/ui/button";
import { DataTable } from "@/shared/ui/data-table";
import { Dialog, DialogTrigger } from "@/shared/ui/dialog";
import { Input } from "@/shared/ui/input";
import { PaginationFooter } from "@/shared/ui/pagination";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { SortingState } from "@tanstack/react-table";
import { Palette, PlusCircle, SearchIcon } from "lucide-react";
import { useState } from "react";
import { useDebouncedCallback } from "use-debounce";
import { z } from "zod";

const SearchSchema = GetCarsQuerySchema;
export const Route = createFileRoute("/_auth/cars")({
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
    if (newValue.length == 0) {
      setQuery((prev) => ({
        ...prev,
        sortBy: undefined,
        sortDirection: undefined,
      }));
      return;
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
  }, 500);

  const debouncedSetModel = useDebouncedCallback((model: string) => {
    setQuery((prev) => ({
      ...prev,
      model,
    }));
  }, 500);

  const debouncedSetColor = useDebouncedCallback((color: string) => {
    setQuery((prev) => ({
      ...prev,
      color,
    }));
  }, 500);
  const [open, setOpen] = useState(false);

  return (
    <div className="space-y-4">
      <header className="flex flex-col gap-2 lg:flex-row">
        <Dialog open={open} onOpenChange={setOpen}>
          <DialogTrigger asChild>
            <Button variant="outline" size="sm">
              <PlusCircle className="mr-2 size-4" />
              Создать
            </Button>
          </DialogTrigger>
          <CreateCarDialogContent onClose={() => setOpen(false)} />
        </Dialog>
        <div className="flex flex-col items-end gap-2 lg:ml-auto lg:flex-row [&>*]:w-full [&_span]:w-32 lg:[&_span]:w-auto lg:[&>*]:w-auto">
          <div className="flex">
            <span className="inline-flex h-8 items-center rounded-md rounded-r-none border border-r-0 border-input px-2 align-middle text-sm">
              <SearchIcon className="mr-2 size-4" />
              Брэнд
            </span>
            <Input
              className="h-8 flex-grow rounded-l-none ring-0"
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
          <div className="flex">
            <span className="inline-flex h-8 items-center rounded-md rounded-r-none border border-r-0 border-input px-2 align-middle text-sm">
              <Palette className="mr-2 size-4" />
              Цвет
            </span>
            <Input
              className="h-8 rounded-l-none ring-0"
              defaultValue={search.color}
              onChange={(e) => debouncedSetColor(e.target.value)}
            />
          </div>
        </div>
      </header>
      <main>
        <DataTable table={table} />
      </main>
      <PaginationFooter query={search} setQuery={setQuery} total={data.total} />
    </div>
  );
}
