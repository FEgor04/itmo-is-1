import { Badge } from "@/shared/ui/badge";
import { Button } from "@/shared/ui/button";
import {
  ColumnDef,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { Check, X } from "lucide-react";
import { z } from "zod";
import {
  useApproveAdminRequestMutation,
  useRejectAdminRequestMutation,
} from "./api";

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
  {
    id: "actions",
    header: "",
    cell: ({ row }) => {
      return (
        row.original.status == "PENDING" && <Actions request={row.original} />
      );
    },
  },
];

function Actions({ request }: { request: AdminRequest }) {
  const { mutate: approve, isPending: isApprovePending } =
    useApproveAdminRequestMutation();
  const { mutate: reject, isPending: isRejectPending } =
    useRejectAdminRequestMutation();
  const isPending = isApprovePending || isRejectPending;
  return (
    <div className="flex items-center gap-2">
      <Button
        className="size-8 bg-green-600 p-0 text-white hover:bg-green-600/80"
        disabled={isPending}
        onClick={() => approve(request.username)}
      >
        <Check className="h-4 w-4" />
      </Button>
      <Button
        className="size-8 p-0"
        variant="outline"
        disabled={isPending}
        onClick={() => reject(request.username)}
      >
        <X className="h-4 w-4" />
      </Button>
    </div>
  );
}

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
