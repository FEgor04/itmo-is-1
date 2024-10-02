import {
  ColumnDef,
  getCoreRowModel,
  OnChangeFn,
  SortingState,
  useReactTable,
} from "@tanstack/react-table";
import { FetchedHumanBeing } from "./model";
import { MoodBadge } from "../enums/mood";
import { WeaponTypeBadge } from "../enums/weapon-type";

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
    cell: ({ row }) => {
      return <MoodBadge value={row.original.mood} />;
    },
  },
  {
    accessorKey: "impactSpeed",
    header: "Скорость",
  },
  {
    accessorKey: "weaponType",
    header: "Тип оружия",
    cell: ({ row }) => {
      return <WeaponTypeBadge value={row.original.weaponType} />;
    },
  },
];

export function useHumanBeingTable(
  data: Array<FetchedHumanBeing>,
  sortingState: SortingState,
  handleSetSortingState: OnChangeFn<SortingState>,
) {
  return useReactTable({
    getCoreRowModel: getCoreRowModel(),
    data,
    columns: HumanBeingTableDef,
    state: {
      sorting: sortingState,
    },
    onSortingChange: handleSetSortingState,
    defaultColumn: {
      cell: ({ getValue }) => {
        const value = getValue();
        if (value === undefined) {
          return <>&mdash;</>;
        }
        if (typeof value === "boolean") {
          return value ? "Да" : "Нет";
        }
        return value;
      },
    },
  });
}
