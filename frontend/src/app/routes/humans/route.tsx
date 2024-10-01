import { useHumanBeingTable } from "@/entities/human-being/table";
import { DataTable } from "@/shared/ui/data-table";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/humans")({
  component: Page,
});

function Page() {
  const table = useHumanBeingTable([]);
  return <DataTable table={table} />;
}
