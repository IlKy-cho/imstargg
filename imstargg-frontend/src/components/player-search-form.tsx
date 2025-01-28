"use client";

import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";

import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import React from "react";
import {Form, FormControl, FormField, FormItem, FormMessage} from "@/components/ui/form";
import {useRouter} from "next/navigation";

const formSchema = z.object({
  nameOrTag: z.string().min(0).max(100, {
    message: "100자 이하로 입력해주세요."
  }),
});

export default function PlayerSearchForm() {
  const router = useRouter();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      nameOrTag: "",
    },
  });

  function onSubmit(value: z.infer<typeof formSchema>) {
    if (value.nameOrTag.trim()) {
      const searchQuery = value.nameOrTag.startsWith('#') ?
        encodeURIComponent(value.nameOrTag) : `${value.nameOrTag}`;
      router.push(`/player/search?q=${searchQuery}`);
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="flex gap-1">
        <FormField
          control={form.control}
          name="nameOrTag"
          render={({field}) => (
            <FormItem className="flex-1">
              <FormControl>
                <Input
                  className="bg-white"
                  placeholder="플레이어 태그 / 이름" {...field}
                />
              </FormControl>
              <FormMessage/>
            </FormItem>
          )}
        />
        <Button type="submit">검색</Button>
      </form>
    </Form>
  );
}