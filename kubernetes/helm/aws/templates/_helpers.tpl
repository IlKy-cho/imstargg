{{- define "aws.secretName" -}}
{{- if .Values.app.name -}}
aws-secret-{{ .Values.app.name }}
{{- else -}}
aws-secret
{{- end -}}
{{- end -}}