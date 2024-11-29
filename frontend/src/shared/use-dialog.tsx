import { ComponentProps, useCallback, useState } from "react";
import { Dialog as ShadcnDialog } from "@/shared/ui/dialog";

type DialogProps = Omit<
  ComponentProps<typeof ShadcnDialog>,
  "open" | "onOpenChange"
>;

export function useDialog() {
  const [open, setOpen] = useState(false);

  const Dialog = useCallback(
    (props: DialogProps) => {
      return <ShadcnDialog open={open} onOpenChange={setOpen} {...props} />;
    },
    [open, setOpen],
  );

  const onClose = useCallback(() => {
    setOpen(false);
  }, [setOpen]);

  return { open, setOpen, Dialog, onClose };
}
