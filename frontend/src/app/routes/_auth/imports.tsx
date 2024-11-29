import { createFileRoute, notFound } from "@tanstack/react-router";
import {
  getImportsQueryOptions,
  getImportsRequestSchema,
  UploadImportDialogContent,
  useImportsTable,
} from "@/entities/import";
import { useQuery } from "@tanstack/react-query";
import { CrudControlsHeader } from "@/shared/ui/controls.tsx";
import { DataTable } from "@/shared/ui/data-table.tsx";
import { PaginationFooter } from "@/shared/ui/pagination.tsx";
import { DialogTrigger } from "@/shared/ui/dialog.tsx";
import { useDialog } from "@/shared/use-dialog.tsx";
import { Button } from "@/shared/ui/button.tsx";
import { UploadIcon } from "lucide-react";
import { featureFlags } from "@/shared/config.ts";

export const Route = createFileRoute("/_auth/imports")({
  validateSearch: getImportsRequestSchema,
  loaderDeps: ({ search }) => search,
  loader: ({ deps, context }) => {
    return context.queryClient.ensureQueryData(getImportsQueryOptions(deps));
  },
  beforeLoad: () => {
    if(!featureFlags.importPage) {
      throw notFound();
    }
  },
  component: Page,
});

function Page() {
  const query = Route.useSearch();
  const navigate = Route.useNavigate();
  const initialData = Route.useLoaderData();
  const { data } = useQuery({ ...getImportsQueryOptions(query), initialData });
  const values = data.values;
  const { Dialog, onClose } = useDialog();

  const table = useImportsTable(values);

  return (
    <div className="space-y-4">
      <CrudControlsHeader>
        <div>
          <Dialog>
            <DialogTrigger asChild>
              <Button variant="outline" size="sm">
                <UploadIcon className="mr-2 size-4" />
                Импорт
              </Button>
            </DialogTrigger>
            <UploadImportDialogContent onClose={onClose} />
          </Dialog>
        </div>
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
