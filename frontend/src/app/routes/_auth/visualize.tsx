import {
  getHumansQueryOptions,
  GetHumansQuerySchema,
} from "@/entities/human-being/api";
import { CreateHumanBeingDialogContent } from "@/entities/human-being/ui/create";
import { HumansVisualization } from "@/entities/human-being/ui/visualization";
import { Button } from "@/shared/ui/button";
import { CrudControlsHeader, CrudControlsRight } from "@/shared/ui/controls";
import { Dialog } from "@/shared/ui/dialog";
import { Input } from "@/shared/ui/input";
import { PaginationFooter } from "@/shared/ui/pagination";
import { DialogTrigger } from "@radix-ui/react-dialog";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { Gauge, PlusCircle, SearchIcon } from "lucide-react";
import { useState } from "react";
import { useDebouncedCallback } from "use-debounce";
import { z } from "zod";

const SearchSchema = GetHumansQuerySchema;

export const Route = createFileRoute("/_auth/visualize")({
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
  const initialData = Route.useLoaderData();
  const { data } = useQuery({ ...getHumansQueryOptions(query), initialData });

  function setQuery(
    updater: (
      previous: z.infer<typeof SearchSchema>,
    ) => z.infer<typeof SearchSchema>,
  ) {
    void navigate({
      search: updater,
    });
  }

  const setNameFilter = useDebouncedCallback((name: string | undefined) => {
    setQuery((prev) => ({
      ...prev,
      name: name && name?.length > 0 ? name : undefined,
    }));
  }, 500);

  const setImpactSpeed = useDebouncedCallback((value: string | undefined) => {
    setQuery((prev) => ({
      ...prev,
      impactSpeedLowerThan:
        value && value?.length > 0 ? parseInt(value) : undefined,
    }));
  }, 500);
  const [createOpen, setCreateOpen] = useState(false);

  return (
    <div className="space-y-4">
      <CrudControlsHeader>
        <div>
          <Dialog open={createOpen} onOpenChange={setCreateOpen}>
            <DialogTrigger asChild>
              <Button size="sm" variant="outline">
                <PlusCircle className="mr-2 size-4" />
                Создать
              </Button>
            </DialogTrigger>
            <CreateHumanBeingDialogContent
              onClose={() => setCreateOpen(false)}
            />
          </Dialog>
        </div>
        <CrudControlsRight>
          <div className="inline-flex items-center">
            <span className="inline-flex h-8 items-center rounded-md rounded-r-none border border-r-0 border-input px-2 align-middle text-sm">
              <SearchIcon className="mr-2 size-4" />
              Имя
            </span>
            <Input
              className="h-8 rounded-l-none ring-0"
              defaultValue={query.name}
              onChange={(e) => setNameFilter(e.target.value)}
            />
          </div>
          <div className="inline-flex items-center">
            <span className="inline-flex h-8 items-center rounded-md rounded-r-none border border-r-0 border-input px-2 align-middle text-sm">
              <Gauge className="mr-2 size-4" />
              Скорость
            </span>
            <Input
              type="number"
              className="h-8 rounded-l-none ring-0"
              defaultValue={query.impactSpeedLowerThan}
              onChange={(e) => setImpactSpeed(e.target.value)}
            />
          </div>
        </CrudControlsRight>
      </CrudControlsHeader>
      <main>
        <HumansVisualization humans={data.values} />
      </main>
      <PaginationFooter query={query} setQuery={setQuery} total={data.total} />
    </div>
  );
}
