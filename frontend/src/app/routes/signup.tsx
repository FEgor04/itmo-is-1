import { SignInSchema, useSignUpMutation } from "@/shared/auth";
import { Button } from "@/shared/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/shared/ui/card";
import { Form, FormField, FormItem, FormMessage } from "@/shared/ui/form";
import { Input } from "@/shared/ui/input";
import { Label } from "@/shared/ui/label";
import { zodResolver } from "@hookform/resolvers/zod";
import { createFileRoute, Link, useNavigate } from "@tanstack/react-router";
import { AxiosError } from "axios";
import { useForm } from "react-hook-form";
import { z } from "zod";

export const Route = createFileRoute("/signup")({
  component: Page,
});
const schema = SignInSchema;
type Values = z.infer<typeof schema>;

function Page() {
  const form = useForm<Values>({
    resolver: zodResolver(schema),
  });
  const { mutate, error, isPending } = useSignUpMutation();
  const navigate = useNavigate();

  function onSubmit(data: Values) {
    console.log(data);
    mutate(data, {
      onSuccess: () => {
        navigate({
          to: "/humans",
          search: {
            page: 1,
            pageSize: 25,
          },
        });
      },
    });
  }

  return (
    <div>
      <Card className="mx-auto mt-8 max-w-lg">
        <CardHeader>
          <CardTitle>Регистрация</CardTitle>
          <CardDescription>Регистрация нового пользователя</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form id="signup" onSubmit={form.handleSubmit(onSubmit)}>
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <Label>Имя пользователя</Label>
                    <Input required {...field} />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <Label>Пароль</Label>
                    <Input type="password" required {...field} />
                  </FormItem>
                )}
              />
            </form>
            {error && <SignUpError error={error} />}
          </Form>
        </CardContent>
        <CardFooter>
          <Button type="submit" form="signup" disabled={isPending}>
            Отправить
          </Button>
          <Button asChild variant="link">
            <Link to="/signin">Войти</Link>
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}

function SignUpError({ error }: { error: Error }) {
  if (!(error instanceof AxiosError)) {
    return <FormMessage>Не удалось зарегистрировать пользователя</FormMessage>;
  }
  if (error.response?.status === 409) {
    return <FormMessage>Такой пользователь уже существует</FormMessage>;
  }
  return <FormMessage>Не удалось зарегистрировать пользователя</FormMessage>;
}
