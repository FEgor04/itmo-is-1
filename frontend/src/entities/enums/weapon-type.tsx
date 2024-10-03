import { Badge } from "@/shared/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/shared/ui/select";
import { z } from "zod";

export const WeaponTypeSchema = z.enum(["AXE", "PISTOL", "MACHINE_GUN"])

export type WeaponType = z.infer<typeof WeaponTypeSchema>

export const WeaponTypeTranslation = {
    AXE: "Топор",
    PISTOL: "Пистолет",
    MACHINE_GUN: "Пулемет"
} satisfies Record<WeaponType, string>

const WeaponTypeStyle = {
    AXE: "bg-red-200 text-red-600 hover:bg-red-300",
    PISTOL: "bg-blue-200 text-blue-600 hover:bg-blue-300",
    MACHINE_GUN: "bg-gray-200 text-gray-600 hover:bg-gray-300",
  } satisfies Record<WeaponType, string>;
  
  export function WeaponTypeBadge({ value }: { value: WeaponType }) {
    return (
      <Badge
        className={WeaponTypeStyle[value]}
      >
        {WeaponTypeTranslation[value]}
      </Badge>
    );
  }

type Props = {
  value: WeaponType | undefined;
  onChange: (value: WeaponType) => void;
} & Omit<React.ComponentProps<typeof Select>, "value" | "onChange">;

export const SelectWeaponType: React.FC<Props> = ({ value, onChange, ...props }) => {
  return (
    <Select
      value={value}
      onValueChange={(value) => onChange(WeaponTypeSchema.parse(value))}
      {...props}
    >
      <SelectTrigger>
        <SelectValue />
      </SelectTrigger>
      <SelectContent>
        {WeaponTypeSchema.options.map((option) => (
          <SelectItem value={option} key={option}>
            {WeaponTypeTranslation[option]}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
