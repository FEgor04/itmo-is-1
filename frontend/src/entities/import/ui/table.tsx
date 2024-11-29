import {
  ColumnDef,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { Import } from "@/entities/import";
import { ImportStatusBadge } from "@/entities/import/ui/status.tsx";

export const columns: Array<ColumnDef<Import>> = [
  {
    accessorKey: "id",
    header: "ID",
  },
  {
    accessorKey: "status",
    header: "Статус",
    cell: ({ row }) => <ImportStatusBadge status={row.original.status} />,
  },
  {
    accessorKey: "author.username",
    header: "Пользователь",
  },
  {
    accessorKey: "createdEntities",
    header: () => <div className="text-right">Кол-во добавленых сущностей</div>,
    cell: ({ row: { original } }) => (
      <div className="text-right">
        {original.status == "finished" ? (
          original.createdEntities
        ) : (
          <>&mdash;</>
        )}
      </div>
    ),
  },
  {
    accessorKey: "startedAt",
    header: "Дата начала",
  },
  {
    accessorKey: "finishedAt",
    header: "Дата конца",
  },
];

export function useImportsTable(data: Array<Import>) {
  return useReactTable({
    getCoreRowModel: getCoreRowModel(),
    data,
    columns,
    enableSorting: false,
    defaultColumn: {
      cell: ({ getValue }) => {
        const value = getValue();
        if (value === undefined) {
          return <>&mdash;</>;
        }
        if (typeof value === "boolean") {
          return value ? "Да" : "Нет";
        }
        if (value instanceof Date) {
          return value.toLocaleString();
        }
        return value;
      },
    },
  });
}
