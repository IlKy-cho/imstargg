{{- define "aws.secretName" -}}
{{- if .Values.app.name }}
aws-{{ .Values.app.name }}-secret
{{- else }}
aws-secret
{{- end }}
{{- end }}