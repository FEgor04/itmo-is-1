import { Badge } from "@/shared/ui/badge";
import z from "zod";
export const MoodSchema = z.enum(["SORROW", "APATHY", "CALM", "FRENZY"]);

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
  return (
    <Badge
      className={MoodStyle[value]}
    >
      {MoodTranslation[value]}
    </Badge>
  );
}
