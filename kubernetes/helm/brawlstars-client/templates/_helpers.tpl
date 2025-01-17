{{- define "brawlStarsClient.secretName" -}}
{{- if .Values.app.name -}}
brawlstars-client-secret-{{ .Values.app.name }}
{{- else -}}
brawlstars-client-secret
{{- end -}}
{{- end -}}

{{- define "client.keys" -}}
{{- join "," .Values.client.keys }}
{{- end }}