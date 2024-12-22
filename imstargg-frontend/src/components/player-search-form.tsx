"use client";

import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";

import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import React from "react";
import {Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";

const formSchema = z.object({
  nameOrTag: z.string().min(0).max(100, {
    message: "100자 이하로 입력해주세요."
  }),
});

export default function PlayerSearchForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      nameOrTag: "",
    },
  });

  function onSubmit(value: z.infer<typeof formSchema>) {
    console.log(`검색어 값: ${value.nameOrTag}`);
  }

  return (
    <div className="w-full max-w-xl">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="flex gap-1">
          <FormField
            control={form.control}
            name="nameOrTag"
            render={({ field }) => (
              <FormItem className="flex-1">
                <FormControl>
                  <Input placeholder="플레이어 태그 혹은 이름을 선택해주세요." {...field}/>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit">검색</Button>
        </form>
      </Form>
    </div>
  );
}