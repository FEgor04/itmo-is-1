import { DialogContent, DialogFooter, DialogHeader } from "@/shared/ui/dialog";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { CreateHumanBeingSchema } from "../api";
import { z } from "zod";
import { Form, FormField, FormItem } from "@/shared/ui/form";
import { Label } from "@/shared/ui/label";
import { Input } from "@/shared/ui/input";
import { Button } from "@/shared/ui/button";

export function CreateHumanBeingDialogContent() {
  const form = useForm<z.infer<typeof CreateHumanBeingSchema>>({
    resolver: zodResolver(CreateHumanBeingSchema),
  });

  function onSubmit(values: z.infer<typeof CreateHumanBeingSchema>) {
    console.log(values);
  }

  return (
    <DialogContent>
      <DialogHeader>Создание Human Being</DialogHeader>
      <form id="create-human-being-form" onSubmit={form.handleSubmit(onSubmit)}>
        <Form {...form}>
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <Label>Имя</Label>
                <Input {...field} />
              </FormItem>
            )}
          />
        </Form>
      </form>
      <DialogFooter>
        <Button form="create-human-being-form">Создать</Button>
      </DialogFooter>
    </DialogContent>
  );
}
