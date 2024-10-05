import { Badge } from "@/shared/ui/badge";
import {
  ColumnDef,
  getCoreRowModel,
  OnChangeFn,
  SortingState,
  useReactTable,
} from "@tanstack/react-table";
import { z } from "zod";

const requestsColumns: Array<ColumnDef<AdminRequest>> = [
  {
    accessorKey: "username",
    header: "Имя пользователя",
  },
  {
    accessorKey: "status",
    header: "Статус",
    cell: ({ row }) => {
      const status = row.original.status;
      if (status == "PENDING") {
        return (
          <Badge variant="outline" className="bg-yellow-200 text-yellow-600">
            Ожидает одобрения
          </Badge>
        );
      }
      if (status == "APPROVED") {
        return (
          <Badge variant="outline" className="bg-green-200 text-green-600">
            Одобрен
          </Badge>
        );
      }
      if (status == "REJECTED") {
        return (
          <Badge variant="outline" className="bg-red-200 text-red-600">
            Отклонен
          </Badge>
        );
      }
    },
  },
];

export const AdminRequestSchema = z.object({
  username: z.string(),
  status: z.enum(["PENDING", "APPROVED", "REJECTED", "NO_REQUEST"]),
});
type AdminRequest = z.infer<typeof AdminRequestSchema>;
export function useRequestsTable(data: AdminRequest[]) {
  return useReactTable({
    getCoreRowModel: getCoreRowModel(),
    data,
    columns: requestsColumns,
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
        return value;
      },
    },
  });
}
