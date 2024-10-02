import { useHumanBeingTable } from "@/entities/human-being/table";
import { DataTable } from "@/shared/ui/data-table";
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/humans")({
  component: Page,
});

function Page() {
  const table = useHumanBeingTable([{
    id: 1,
    name: "asd",
    realHero: true,
    mood: "CALM",
    coordinates: {
      x: 1,
      y: 1
    },
    creationDate: new Date(),
    hasToothpick: true,
    weaponType: "AXE",
    car: {
      cool: true
    },
    impactSpeed: 50
  }]);
  return <DataTable table={table} />;
}
