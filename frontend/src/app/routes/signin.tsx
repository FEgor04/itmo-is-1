import { SignUpSchema, useSignInMutation } from "@/shared/auth";
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

export const Route = createFileRoute("/signin")({
  component: Page,
});
const schema = SignUpSchema;
type Values = z.infer<typeof schema>;

function Page() {
  const form = useForm<Values>({
    resolver: zodResolver(schema),
  });
  const { mutate, error, isPending } = useSignInMutation();
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
          <CardTitle>Вход</CardTitle>
          <CardDescription>
            На этой странице вы можете войти в систему
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form
              id="signin"
              onSubmit={form.handleSubmit(onSubmit)}
              className="space-y-4"
            >
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <Label>Имя пользователя</Label>
                    <Input required {...field} />
                    <FormMessage />
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
                    <FormMessage />
                  </FormItem>
                )}
              />
              {error && <SignInError error={error} />}
            </form>
          </Form>
        </CardContent>
        <CardFooter>
          <Button type="submit" form="signin" disabled={isPending}>
            Войти
          </Button>
          <Button asChild variant="link">
            <Link to="/signup">Зарегистрироваться</Link>
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}

function SignInError({ error }: { error: Error }) {
  if (!(error instanceof AxiosError)) {
    return <FormMessage>Не удалось зарегистрировать пользователя</FormMessage>;
  }
  if (error.response?.status === 401) {
    return <FormMessage>Неверный логин или пароль</FormMessage>;
  }
  return <FormMessage>Не удалось зарегистрировать пользователя</FormMessage>;
}
