using ExamplesLinq;
using System.Linq;
using System.Reflection;
namespace lab7;
class Program
{
    static void Main()
    {
        List<StudentWithTopics> studentsGenerated = Generator.GenerateStudentsWithTopicsEasy();

        Console.WriteLine("\nZadanie 1");
        // zad 1
        SortAndGroupStudents(in studentsGenerated).ForEach(
            group =>
            {
                group.ForEach(student => Console.WriteLine(student));
                Console.WriteLine();
            }
        );
        Console.WriteLine("\nZadanie 2a");
        // zad 2 a
        SortTopicsByCount(in studentsGenerated).ForEach(topic => Console.WriteLine($"{topic.Topic}, count: {topic.Count}"));
        // zad 2 b
        Console.WriteLine("\nZadanie 2b");
        SortTopicsByCountGrouped(in studentsGenerated).ForEach(group =>
        {
            Console.WriteLine($"{group.Gender}\n");
            group.Topics.ForEach(topic => Console.WriteLine($"{topic.Topic}, count: {topic.Count}"));
        }
        );
        // zad 3
        Console.WriteLine("\nZadanie 3");
        var (students, topics) = StudentWithTopic_to_Student(in studentsGenerated);
        students.ForEach(Console.WriteLine);
        topics.ForEach(Console.WriteLine);

        // zad 3 c
        Console.WriteLine("\nZadanie 3c");
        (students, topics, var studentToTopics) = StudentWithTopicToStudenToTopic(in studentsGenerated);
        var joined = from student in students
                     join studentToTopic in studentToTopics on student.Id equals studentToTopic.StudentId
                     join topic in topics on studentToTopic.TopicId equals topic.Id
                     group topic by new { student.Id, student.Name } into studentGroup
                     select new
                     {
                         StudentId = studentGroup.Key.Id,
                         StudentName = studentGroup.Key.Name,
                         Topics = studentGroup.Select(t => t.Name).ToList()
                     };

        foreach (var student in joined)
        {
            Console.WriteLine($"{student.StudentId}, {student.StudentName}, topics: {string.Join(", ", student.Topics)}");
        }

        // zad 4
        Console.WriteLine("\nZadanie 4");
#pragma warning disable
        Type calcType = Type.GetType("lab7.Calculator");
        object obj = Activator.CreateInstance(calcType, new object[] { 5 });
        MethodInfo method = obj.GetType().GetMethod("Multiply");
        object result = method.Invoke(obj, new object[] { 3 });
        Console.WriteLine(result);
#pragma warning restore






    }

    // zad 1
    static List<List<StudentWithTopics>> SortAndGroupStudents(in List<StudentWithTopics> students, int groupSize = 3)
    {

        var groupedStudents =
            (from student in students.Select((student, index) => new { student, index })
             orderby student.student.Name, student.student.Index
             group student.student by student.index / groupSize into groupStudents
             select groupStudents.ToList())
            .ToList();

        return groupedStudents;
    }

    // zad 2 a
    static List<(string Topic, int Count)> SortTopicsByCount(in List<StudentWithTopics> students)
    {
        var topics =
            (from student in students
             from topic in student.Topics
             group topic by topic into topicGroup
             orderby topicGroup.Count() descending, topicGroup.Key
             select (Topic: topicGroup.Key, Count: topicGroup.Count())).ToList();

        return topics;
    }
    // zad 2 b
    static List<(Gender Gender, List<(string Topic, int Count)> Topics)> SortTopicsByCountGrouped(in List<StudentWithTopics> students)
    {
        var groupGenders =
            from student in students
            group student by student.Gender into genderGroup
            select (
                Gender: genderGroup.Key,
                Topics: SortTopicsByCount(genderGroup.ToList())
            );
        return groupGenders.ToList();
    }

    static List<Topic> GetTopics(in List<StudentWithTopics> students)
    {
        var topics = students
            .SelectMany(student => student.Topics)
            .Distinct()
            .Select((topic, index) => new Topic(index, topic))
            .ToList();
        return topics;
    }

    static (List<Student> students, List<Topic> topics) StudentWithTopic_to_Student(in List<StudentWithTopics> students)
    {
        List<Topic> topics = GetTopics(in students);
        var studentTopicIds = students
            .Select(student =>
                {
                    return new Student(
                        student.Id,
                        student.Index,
                        student.Name,
                        student.Gender,
                        student.Active,
                        student.DepartmentId,
                        student.Topics
                            .Select(t => topics.First(topic => topic.Name == t).Id)
                            .ToList()
                    );
                }
            ).ToList();

        return (studentTopicIds, topics);
    }

    static (List<Student> students, List<Topic> topics, List<StudentToTopic> studentToTopics) StudentWithTopicToStudenToTopic(in List<StudentWithTopics> students)
    {
        List<Topic> topics = GetTopics(in students);
        List<StudentToTopic> studentToTopics = [];
        var studentTopicIds = students
            .Select(student =>
                {
                    foreach (var topic in student.Topics)
                    {
                        studentToTopics.Add(new StudentToTopic(student.Id, topics.First(t => t.Name == topic).Id));
                    }
                    return new Student(
                        student.Id,
                        student.Index,
                        student.Name,
                        student.Gender,
                        student.Active,
                        student.DepartmentId
                    );
                }
            ).ToList();

        return (studentTopicIds, topics, studentToTopics);
    }



    class Student(int id, int index, string name, Gender gender, bool active,
        int departmentId, List<int>? topics = null)
    {
        public int Id { get; set; } = id;
        public int Index { get; set; } = index;
        public string Name { get; set; } = name;
        public Gender Gender { get; set; } = gender;
        public bool Active { get; set; } = active;
        public int DepartmentId { get; set; } = departmentId;

        public List<int>? Topics { get; set; } = topics;

        public override string ToString()
        {
            var result = $"{Id,2}) {Index,5}, {Name,11}, {Gender,6},{(Active ? "active" : "no active"),9},{DepartmentId,2}, ";
            if (Topics != null)
            {
                result += "topics: ";
                foreach (var topic in Topics)
                    result += topic + ", ";
            }
            return result;
        }
    }

    class Topic(int id, string name)
    {
        public int Id { get; set; } = id;
        public string Name { get; set; } = name;

        public override string ToString()
        {
            return $"{Id,2}, {Name}";
        }
    }

    class StudentToTopic(int studentId, int topicId)
    {
        public int StudentId { get; set; } = studentId;
        public int TopicId { get; set; } = topicId;

        public override string ToString()
        {
            return $"{StudentId}, {TopicId}";
        }
    }


}
class Calculator
{
    public int Multiplier { get; set; }
    public Calculator(int multiplier)
    {
        Multiplier = multiplier;
    }
    public int Multiply(int x)
    {
        return x * Multiplier;
    }
}



