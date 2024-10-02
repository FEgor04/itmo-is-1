import { useHumanBeingTable } from "@/entities/human-being/table";
import { PaginatedQuerySchema } from "@/shared/pagination";
import { DataTable } from "@/shared/ui/data-table";
import { createFileRoute } from "@tanstack/react-router";

const SearchSchema = PaginatedQuerySchema;

export const Route = createFileRoute("/humans")({
  component: Page,
  validateSearch: SearchSchema,
});

function Page() {
  const table = useHumanBeingTable([
    {
      id: 1,
      name: "asd",
      realHero: true,
      mood: "CALM",
      coordinates: {
        x: 1,
        y: 1,
      },
      creationDate: new Date(),
      hasToothpick: true,
      weaponType: "AXE",
      car: {
        cool: true,
      },
      impactSpeed: 50,
    },
  ]);
  return <DataTable table={table} />;
}
