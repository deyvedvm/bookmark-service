aws cloudformation create-stack --stack-name S3ToSQSStack --template-body file://bookmark-csv.yaml --capabilities CAPABILITY_NAMED_IAM --profile deyvedev

aws cloudformation update-stack --stack-name S3ToSQSStack --template-body file://bookmark-csv.yaml --capabilities CAPABILITY_NAMED_IAM --profile deyvedev

aws cloudformation describe-stacks --stack-name S3ToSQSStack --profile deyvedev

aws cloudformation delete-stack --stack-name S3ToSQSStack --profile deyvedev


