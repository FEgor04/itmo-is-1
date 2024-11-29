import { Import } from "@/entities/import";
import { Badge } from "@/shared/ui/badge.tsx";

export function ImportStatusBadge({ status }: { status: Import["status"] }) {
  if (status == "error") {
    return <Badge variant="destructive">Ошибка</Badge>;
  }
  if (status == "inProgress") {
    return (
      <Badge
        variant="default"
        className="bg-blue-200 text-blue-600 hover:bg-blue-200/80"
      >
        Ошибка
      </Badge>
    );
  }

  if (status == "finished") {
    return (
      <Badge
        variant="default"
        className="bg-green-200 text-green-600 hover:bg-green-200/80"
      >
        Завершено
      </Badge>
    );
  }
}
