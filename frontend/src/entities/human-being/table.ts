import {
  ColumnDef,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { FetchedHumanBeing } from "./model";

const HumanBeingTableDef: Array<ColumnDef<FetchedHumanBeing>> = [
  {
    accessorKey: "id",
    header: "ID",
  },
  {
    accessorKey: "name",
    header: "Имя",
  },
  {
    accessorKey: "coordinates.x",
    header: "X",
  },
  {
    accessorKey: "coordinates.y",
    header: "Y",
  },
  {
    accessorKey: "creationDate",
    header: "Дата создания",
    cell: ({ row }) => {
      return row.original.creationDate.toLocaleDateString();
    },
  },
  {
    accessorKey: "realHero",
    header: "Real Hero?",
  },
  {
    accessorKey: "hasToothpick",
    header: "Есть зубочистка?",
  },
  {
    accessorKey: "mood",
    header: "Настроение",
  },
  {
    accessorKey: "impactSpeed",
    header: "Скорость",
  },
  {
    accessorKey: "weaponType",
    header: "Тип оружия",
  },
];

export function useHumanBeingTable(data: Array<FetchedHumanBeing>) {
  return useReactTable({
    getCoreRowModel: getCoreRowModel(),
    data,
    columns: HumanBeingTableDef,
  });
}
