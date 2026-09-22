$TOKEN="eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqYW5lQGV4YW1wbGUuY29tIiwiaWF0IjoxNzkwMDcxOTU0LCJleHAiOjE3OTAxNTgzNTR9.aztipes4rYs_o-48Ci4kBLk8VjCGH-pmpjbN4QwEyHnqo2nLRe64HPzXk-mUkhrC"

Write-Host "--- LOGIN RESPONSE ---"
$loginBody = @{
    email = "jane@example.com"
    password = "SecurePassword123!"
} | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/login" -ContentType "application/json" -Body $loginBody | ConvertTo-Json

Write-Host "`n--- CREATE PROJECT RESPONSE ---"
$projectBody = @{
    title = "Test Project"
    description = "A project for testing evaluate endpoint"
} | ConvertTo-Json
$projectRes = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/projects" -ContentType "application/json" -Headers @{Authorization="Bearer $TOKEN"} -Body $projectBody
$projectRes | ConvertTo-Json
$PROJECT_ID = $projectRes.id

Write-Host "`n--- CREATE PROMPT VERSION 1 ---"
$v1Body = @{
    versionLabel = "v1"
    systemPromptText = "You are a polite assistant. Answer in one sentence."
} | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/projects/$PROJECT_ID/versions" -ContentType "application/json" -Headers @{Authorization="Bearer $TOKEN"} -Body $v1Body | ConvertTo-Json

Write-Host "`n--- CREATE PROMPT VERSION 2 ---"
$v2Body = @{
    versionLabel = "v2"
    systemPromptText = "You are a grumpy assistant. Answer in one word."
} | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/projects/$PROJECT_ID/versions" -ContentType "application/json" -Headers @{Authorization="Bearer $TOKEN"} -Body $v2Body | ConvertTo-Json

Write-Host "`n--- CREATE TEST INPUT 1 ---"
$i1Body = @{
    inputText = "What is the capital of France?"
    expectedKeywords = "Paris"
} | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/projects/$PROJECT_ID/inputs" -ContentType "application/json" -Headers @{Authorization="Bearer $TOKEN"} -Body $i1Body | ConvertTo-Json

Write-Host "`n--- CREATE TEST INPUT 2 ---"
$i2Body = @{
    inputText = "Who wrote Hamlet?"
    expectedKeywords = "Shakespeare"
} | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/projects/$PROJECT_ID/inputs" -ContentType "application/json" -Headers @{Authorization="Bearer $TOKEN"} -Body $i2Body | ConvertTo-Json

Write-Host "`n--- RUN EVALUATION RESPONSE (JUDGE ON) ---"
try {
    $evalRes = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/projects/$PROJECT_ID/evaluate" -ContentType "application/json" -Headers @{Authorization="Bearer $TOKEN"} -Body "{}"
    $evalRes | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error evaluating:"
    $_ | Format-List * -Force
}

Write-Host "`n--- RUN EVALUATION RESPONSE (JUDGE OFF) ---"
try {
    $evalBody = @{
        useJudgeScoring = $false
    } | ConvertTo-Json
    $evalRes2 = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/projects/$PROJECT_ID/evaluate" -ContentType "application/json" -Headers @{Authorization="Bearer $TOKEN"} -Body $evalBody
    $evalRes2 | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error evaluating:"
    $_ | Format-List * -Force
}
