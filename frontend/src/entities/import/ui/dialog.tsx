import {
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle
} from "@/shared/ui/dialog.tsx";
import { Upload } from "lucide-react";
import { useRef } from "react";
import { useUploadImportMutation } from "@/entities/import";
import { toast } from "sonner";

type Props = {
  onClose: () => void;
};

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export function UploadImportDialogContent({onClose}: Props) {
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const {mutate} = useUploadImportMutation()

  function uploadFile(file: File) {
    const id = toast.loading("Импорт начался, ждите...")
    mutate(file, {
      onSuccess: (data) => {
        if(data.status == "finished") {
          toast.success(`Импорт успешно завершен! Добавлено сущностей: ${data.createdEntities}`, {id})
        }

        if(data.status == "error") {
          toast.error(`Импорт завершен с ошибкой ${data.message}`, {id})
        }
      },
      onError: (err) => {
        toast.error(`Импорт завершен с ошибкой ${err}`, {id})
      },
      onSettled: () => {
        onClose()
      }
    })
  }

  return (
    <DialogContent>
      <DialogHeader>
        <DialogTitle>
          Загрузите файл
        </DialogTitle>
        <DialogDescription>
          Загрузите csv файл с сущностями чтобы импортировать их в БД
        </DialogDescription>
      </DialogHeader>
      <div
        className="border border-dashed w-full h-52 cursor-pointer rounded-lg border-muted-foreground hover:bg-muted/25 flex flex-col items-center justify-center"
        onClick={() => {
          fileInputRef?.current?.click();
        }}>
        <div className="border border-muted rounded-full border-dashed size-12 flex items-center justify-center">
          <Upload className="size-7 text-muted-foreground" />
        </div>
        <p className="font-medium text-muted-foreground">
          Нажмите чтобы загрузить файл
        </p>
        <p className="text-sm text-muted-foreground/70 line-through">
          Или перенесите их сюда
        </p>
        <input ref={fileInputRef} name="import" accept="text/csv" type="file" className="hidden" onChange={(e) => {
          const file = e.target.files ? e.target.files[0] : undefined;
          if(file) {
            uploadFile(file)
          }
        }} />
      </div>
    </DialogContent>
  );
}
