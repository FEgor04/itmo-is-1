import {
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/shared/ui/dialog.tsx";
import { Upload } from "lucide-react";
import { useRef } from "react";
import { useUploadImportMutation } from "@/entities/import";
import { toast } from "sonner";

type Props = {
  onClose: () => void;
};

 
export function UploadImportDialogContent({ onClose }: Props) {
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const { mutate } = useUploadImportMutation();

  function uploadFile(file: File) {
    const id = toast.loading("Импорт начался, ждите...");
    mutate(file, {
      onSuccess: (data) => {
        if (data.status == "finished") {
          toast.success(
            `Импорт успешно завершен! Добавлено сущностей: ${data.createdEntities}`,
            { id },
          );
        }

        if (data.status == "error") {
          toast.error(`Импорт завершен с ошибкой ${data.message}`, { id });
        }
      },
      onError: (err) => {
        toast.error(`Импорт завершен с ошибкой ${err}`, { id });
      },
      onSettled: () => {
        onClose();
      },
    });
  }

  return (
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Загрузите файл</DialogTitle>
        <DialogDescription>
          Загрузите csv файл с сущностями чтобы импортировать их в БД
        </DialogDescription>
      </DialogHeader>
      <div
        className="flex h-52 w-full cursor-pointer flex-col items-center justify-center rounded-lg border border-dashed border-muted-foreground hover:bg-muted/25"
        onClick={() => {
          fileInputRef?.current?.click();
        }}
      >
        <div className="flex size-12 items-center justify-center rounded-full border border-dashed border-muted">
          <Upload className="size-7 text-muted-foreground" />
        </div>
        <p className="font-medium text-muted-foreground">
          Нажмите чтобы загрузить файл
        </p>
        <p className="text-sm text-muted-foreground/70 line-through">
          Или перенесите их сюда
        </p>
        <input
          ref={fileInputRef}
          name="import"
          accept="text/csv"
          type="file"
          className="hidden"
          onChange={(e) => {
            const file = e.target.files ? e.target.files[0] : undefined;
            if (file) {
              uploadFile(file);
            }
          }}
        />
      </div>
    </DialogContent>
  );
}
