import { PropsWithChildren } from "react";

export function CrudControlsHeader({ children }: PropsWithChildren) {
  return (
    <header className="flex flex-col gap-2 lg:flex-row">{children}</header>
  );
}

export function CrudControlsRight({ children }: PropsWithChildren) {
  return (
    <div className="flex flex-col items-end gap-2 lg:ml-auto lg:flex-row [&>*]:w-full lg:[&>*]:w-auto [&_span]:w-40 lg:[&_span]:w-auto">
      {children}
    </div>
  );
}
