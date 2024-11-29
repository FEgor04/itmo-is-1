import {
  DialogContent,
  DialogDescription,
  DialogHeader,
} from "@/shared/ui/dialog.tsx";

type Props = {
  onClose: () => void;
};

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export function UploadImportDialogContent(_props: Props) {
  return (
    <DialogContent>
      <DialogHeader>Загрузите файл</DialogHeader>
      <DialogDescription>
        Загрузите csv файл с сущностями чтобы импортировать их в БД
      </DialogDescription>
    </DialogContent>
  );
}
