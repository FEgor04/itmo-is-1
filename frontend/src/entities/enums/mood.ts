import z from "zod";
export const MoodSchema = z.enum(["SORROW", "APATHY", "CALM", "FRENZY"]);

type Mood = z.infer<typeof MoodSchema>;

export const MoodTranslation = {
  SORROW: "Скорбь",
  APATHY: "Апатия",
  CALM: "Спокойствие",
  FRENZY: "Бешенство",
} satisfies Record<Mood, string>;
