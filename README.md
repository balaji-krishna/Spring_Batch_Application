This project demonstrates the functionalities that are used in spring batch. This project consists of 2 applications.

i. Spring Batch Application

ii. Spring Boot Rest Api Application

The Spring Batch Application consists of all the basic functionalities in spring batch.

The Spring Boot Rest Api Application consists of two rest apis that retrieve a list of entity and save a entity.

Functionalities covered in this project are:

1. Basic Tasklet job that has 2 tasklet steps and fuctions with job listeners.
2. Basic Chunk job that has 2 chunk steps and fuctions with item readers, item processors, item writers, job listeners.
3. Chunk job to read from input csv file and write into output csv file using FlatFileItemReader and FlatFileItemWriter.
4. Chunk job to read from input json file and write into output json file using JsonItemReader and JsonFileItemWriter.
5. Chunk job to read from input xml file and write into output xml file using StaxEventItemReader and StaxEventItemWriter.
6. Chunk job to read from one database and write into another database using JdbcCursorItemReader and JdbcBatchItemWriter.
7. Chunk job to read from one database and write into another database using JpaCursorItemReader and JpaItemWriter.
8. Chunk job to read from another rest api application and write into a csv file and vice versa using ItemReaderAdapter and ItemWriterAdapter.


Operation of jobs:

1. Based on the job name provided in the rest api "/startJob/{jobName}", the respective job is triggered.
2. Based on the job execution id provided in the rest api "/stop/{jobExecutionId}", the respective job is stopped.
3. The paths of input and output files are provided in the request body as job parameters.
4. Async functionality is implemented to return a response to notify the sender that the requested job is started.
5. Job scheduling functionality is implemented for basic chunk job that runs on every minute.
6. Fault tolerant functionality is implemented at the time of reading and writing data in specific jobs. The bad records are skipped and retried and captured in text files. 

Job Results:

The input and output of the jobs can be referred in the inputFiles and outputFiles folders.
