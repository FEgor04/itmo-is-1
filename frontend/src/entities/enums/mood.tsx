import { Badge } from "@/shared/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";
import { REQUIRED_CONFIG } from "@/shared/zod";
import React from "react";
import z from "zod";
export const MoodSchema = z.enum(["SORROW", "APATHY", "CALM", "FRENZY"], REQUIRED_CONFIG);

type Mood = z.infer<typeof MoodSchema>;

export const MoodTranslation = {
  SORROW: "Скорбь",
  APATHY: "Апатия",
  CALM: "Спокойствие",
  FRENZY: "Бешенство",
} satisfies Record<Mood, string>;

const MoodStyle = {
  SORROW: "bg-blue-200 text-blue-600 hover:bg-blue-300",
  APATHY: "bg-gray-200 text-gray-600 hover:bg-blue-300",
  CALM: "bg-green-200 text-green-600 hover:bg-green-300",
  FRENZY: "bg-red-200 text-red-600 hover:bg-red-300",
} satisfies Record<Mood, string>;

export function MoodBadge({ value }: { value: Mood }) {
  return <Badge className={MoodStyle[value]}>{MoodTranslation[value]}</Badge>;
}

type Props = {
  value: Mood | undefined;
  onChange: (value: Mood) => void;
} & Omit<React.ComponentProps<typeof Select>, "value" | "onChange">;

export const SelectMood: React.FC<Props> = ({ value, onChange, ...props }) => {
  return (
    <Select
      value={value}
      onValueChange={(value) => onChange(MoodSchema.parse(value))}
      {...props}
    >
      <SelectTrigger>
        <SelectValue />
      </SelectTrigger>
      <SelectContent>
        {MoodSchema.options.map((option) => (
          <SelectItem value={option} key={option}>
            {MoodTranslation[option]}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
