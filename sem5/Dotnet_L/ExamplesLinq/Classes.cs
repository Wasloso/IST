using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.IO;
using System.Linq;
namespace ExamplesLinq

{
    public class Department(int id, string name)
    {
        public int Id { get; set; } = id;
        public String Name { get; set; } = name;

        public override string ToString()
        {
            return $"{Id,2}), {Name,11}";
        }

    }

    public enum Gender
    {
        Female,
        Male
    }

    public class StudentWithTopics(int id, int index, string name, Gender gender, bool active,
        int departmentId, List<string> topics)
    {
        public int Id { get; set; } = id;
        public int Index { get; set; } = index;
        public string Name { get; set; } = name;
        public Gender Gender { get; set; } = gender;
        public bool Active { get; set; } = active;
        public int DepartmentId { get; set; } = departmentId;

        public List<string> Topics { get; set; } = topics;

        public override string ToString()
        {
            var result = $"{Id,2}) {Index,5}, {Name,11}, {Gender,6},{(Active ? "active" : "no active"),9},{DepartmentId,2}, topics: ";
            foreach (var str in Topics)
                result += str + ", ";
            return result;
        }
    }

    public static class Generator
    {
        public static int[] GenerateIntsEasy()
        {
            return [5, 3, 9, 7, 1, 2, 6, 7, 8];
        }

        public static int[] GenerateIntsMany()
        {
            var result = new int[10000];
            Random random = new ();
            for (int i = 0; i < result.Length; i++)
                result[i] = random.Next(1000);
            return result;
        }

        public static List<string> GenerateNamesEasy()
        {
            return [
                "Nowak",
                "Kowalski",
                "Schmidt",
                "Newman",
                "Bandingo",
                "Miniwiliger",
                "Showner",
                "Neumann",
                "Rocky",
                "Bruno"
            ];
        }
        public static List<StudentWithTopics> GenerateStudentsWithTopicsEasy()
        {
            return [
            new StudentWithTopics(1,12345,"Nowak", Gender.Female,true,1,
                    ["C#","PHP","algorithms"]),
            new StudentWithTopics(2, 13235, "Kowalski", Gender.Male, true,1,
                    ["C#","C++","fuzzy logic"]),
            new StudentWithTopics(3, 13444, "Schmidt", Gender.Male, false,2,
                    ["Basic","Java"]),
            new StudentWithTopics(4, 14000, "Newman", Gender.Female, false,3,
                    ["JavaScript","neural networks"]),
            new StudentWithTopics(5, 14001, "Bandingo", Gender.Male, true,3,
                    ["Java","C#"]),
            new StudentWithTopics(6, 14100, "Miniwiliger", Gender.Male, true,2,
                    ["algorithms","web programming"]),
            new StudentWithTopics(11,22345,"Nowak", Gender.Female,true,2,
                    ["C#","PHP","web programming"]),
            new StudentWithTopics(12, 23235, "Nowak", Gender.Male, true,1,
                    ["C#","C++","fuzzy logic"]),
            new StudentWithTopics(13, 23444, "Showner", Gender.Male, false,2,
                    ["Basic","C#"]),
            new StudentWithTopics(14, 24000, "Neumann", Gender.Female, false,3,
                    ["JavaScript","neural networks"]),
            new StudentWithTopics(15, 24001, "Rocky", Gender.Male, true,2,
                    ["fuzzy logic","C#"]),
            new StudentWithTopics(16, 24100, "Bruno", Gender.Female, false,2,
                    ["algorithms","web programming"]),
            ];
        }

        public static List<Department> GenerateDepartmentsEasy()
        {
            return [
            new Department(1,"Computer Science"),
            new Department(2,"Electronics"),
            new Department(3,"Mathematics"),
            new Department(4,"Mechanics")
            ];
        }

    }
    class Program
    {

        public static void ShowAllCollections()
        {
            Console.WriteLine(nameof(ShowAllCollections));
            Generator.GenerateIntsEasy().ToList().ForEach(Console.WriteLine);
            Generator.GenerateDepartmentsEasy().ForEach(Console.WriteLine);
            Generator.GenerateStudentsWithTopicsEasy().ForEach(Console.WriteLine);
        }

        public static void MethodWhereSimple()
        {
            Console.WriteLine(nameof(MethodWhereSimple));
            var resInt = Generator.GenerateIntsEasy().Where(x => x % 2 == 0);
            resInt.ToList().ForEach(Console.WriteLine);
            var resStr = Generator.GenerateNamesEasy().Where(s => s.Length > 6);
            resStr.ToList().ForEach(Console.WriteLine);
            var resStud = Generator.GenerateStudentsWithTopicsEasy().Where(s => s.Active && s.DepartmentId == 1);
            resStud.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }
        public static void ClauseWhereSimple()
        {
            Console.WriteLine(nameof(ClauseWhereSimple)); 
            var resInt = from v in Generator.GenerateIntsEasy()
                         where v % 2 == 0
                         select v;
            resInt.ToList().ForEach(Console.WriteLine);
            var resStr = from s in Generator.GenerateNamesEasy()
                         where s.Length > 6
                         select s;
            resStr.ToList().ForEach(Console.WriteLine);
            var resStud = from s in Generator.GenerateStudentsWithTopicsEasy()
                          where s.Active && s.DepartmentId == 1
                          select s;
            resStud.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }

        public static void SimpleAggregiates()
        {
            Console.WriteLine(nameof(SimpleAggregiates));
            var ints = Generator.GenerateIntsEasy();
            var resMin = ints.Where(x => x % 2 == 0).Min();
            Console.WriteLine(resMin);
            var resMax = ints.Where(x => x % 2 == 0).Max();
            Console.WriteLine(resMin);
            var strs = Generator.GenerateNamesEasy();
            var resStrMin1 = strs.Min(); //dictionaryOrder
            Console.WriteLine(resStrMin1);
            var resStrMin2 = strs.Min(s => s.Length); // minimum length
            Console.WriteLine(resStrMin2);
            Console.WriteLine("--------");
        }

        public static void ComplexAggregiates()
        {
            Console.WriteLine(nameof(ComplexAggregiates));
            var strs = Generator.GenerateNamesEasy();
            var strs0 = new string[] { };
            var strs1 = new string[] { "one" };
            Console.WriteLine("----- first form, one argument: lambda >>>>>>>>");
            Console.WriteLine(strs.Aggregate((a, b) => a + "," + b));
            try { Console.WriteLine(strs0.Aggregate((a, b) => a + "," + b)); }
            catch (InvalidOperationException e) { Console.WriteLine(e.Message); }
            Console.WriteLine(strs1.Aggregate((a, b) => a + "," + b));
            Console.WriteLine("----- second form, two arguments: accumulator, lambda");
            Console.WriteLine(strs.Aggregate("", (a, b) => a + "," + b));
            Console.WriteLine(strs0.Aggregate("", (a, b) => a + "," + b));
            Console.WriteLine(strs1.Aggregate("", (a, b) => a + "," + b));
            Console.WriteLine("----- third form, three arguments: accumulator, lambda, finish lambda");
            Console.WriteLine(strs.Aggregate("", (a, b) => a + "," + b, res => res.Length));
            Console.WriteLine(strs0.Aggregate("", (a, b) => a + "," + b, res => res.Length));
            Console.WriteLine(strs1.Aggregate("", (a, b) => a + "," + b, res => res.Length));
            Console.WriteLine("----- finding average lenght in a complex way");
            var resStr = strs.Aggregate((0, ""),
                (tuple, str) => (tuple.Item1 + 1, tuple.Item2 + str), res => ((double)res.Item2.Length) / res.Item1);
            Console.WriteLine(resStr);
            //var avrLen= strs.Average(s => s.Length);
            //Console.WriteLine(avrLen);
            Console.WriteLine("--------");
        }

        public static void WhereWithPos()
        {
            Console.WriteLine(nameof(WhereWithPos));
            var resStr = Generator.GenerateNamesEasy()
                .Where((s, pos) => pos % 2 == 0);
            resStr.ToList().ForEach(Console.WriteLine);
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
                .Where((s, pos) => s.Active && pos % 3 == 0);
            resStud.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
            var resStud2 = Generator.GenerateStudentsWithTopicsEasy()
                .Where(s => s.Active)
                .Where((s, pos) => pos % 3 == 0);
            resStud2.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }

        public static void TestSelect()
        {
            Console.WriteLine(nameof(TestSelect)); 
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
                .Where(s => s.Index < 20000)
                .Select(s => new { Header = s.Id + ") " + s.Index, s.Name });
            foreach (var x in resStud)
            {
                Console.WriteLine($" {x.Header} =====> {x.Name}");
            }
            Console.WriteLine("-------------");
            var resStud2 = from s in Generator.GenerateStudentsWithTopicsEasy()
                           where s.Index < 20000
                           select new { Header = s.Id + ") " + s.Index, s.Name };
            foreach (var x in resStud2)
            {
                Console.WriteLine($" {x.Header} =====> {x.Name}");
            }
            Console.WriteLine("-------------");
            var resStud3 = from s in Generator.GenerateStudentsWithTopicsEasy()
                           where s.Index < 20000
                           select (Header: s.Id + ") " + s.Index, s.Name);
            foreach (var x in resStud3)
            {
                Console.WriteLine($" {x.Header} =====> {x.Name}");
            }
            Console.WriteLine("--------");
        }

        public static void TestSelectMany()
        {
            Console.WriteLine(nameof(TestSelectMany));
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
                .Where(s => s.Index < 20000)
                .SelectMany(s => s.Topics);
            resStud.ToList().ForEach(x => Console.Write(x + ";"));
            Console.WriteLine();
            Console.WriteLine(resStud.Count());
            var resChars = resStud
                .SelectMany(s => s);
            resChars.ToList().ForEach(x => Console.Write(x + ";"));
            Console.WriteLine();
            Console.WriteLine(resChars.Count());
            Console.WriteLine("--------");
        }

        public static void TestSelectManyQuery()
        {
            Console.WriteLine(nameof(TestSelectManyQuery));
            var resStud = from s in Generator.GenerateStudentsWithTopicsEasy()
                          where s.Index < 20000
                          from topic in s.Topics
                          select topic;
            resStud.ToList().ForEach(x => Console.Write(x + ";"));
            Console.WriteLine();
            Console.WriteLine(resStud.Count());
            var resChars = from s in resStud
                           from c in s
                           select c;
            resChars.ToList().ForEach(x => Console.Write(x + ";"));
            Console.WriteLine();
            Console.WriteLine(resChars.Count());
            Console.WriteLine("--------");
        }

        public static void TestSelectManyWith2Lambdas()
        {
            Console.WriteLine(nameof(TestSelectManyWith2Lambdas));
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
                .Where(s => s.Index < 20000 && s.Name.Length <= 6)
                .SelectMany(s => s.Topics, (stud, topic) => new { stud.Name, topic });
            resStud.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("----------------");
            var resStud2 = from s in Generator.GenerateStudentsWithTopicsEasy()
                           where s.Index < 20000 && s.Name.Length <= 6
                           from topic in s.Topics
                           select new { s.Name, topic };
            resStud2.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }
        public static void TestOrderBy()
        {
            Console.WriteLine(nameof(TestOrderBy));
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
                .OrderBy(s => s.Name)
                .ThenByDescending(s => s.Index);
            resStud.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("----------------");
            var resStud2 = from s in Generator.GenerateStudentsWithTopicsEasy()
                           orderby s.Name, s.Index descending
                           select s;
            resStud2.ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }

        class MyComparer : IComparer<string>
        {
            int IComparer<string>.Compare(string x, string y)
            {
                return x.Length - y.Length;
            }
        }
        public static void TestOrderByWithComparer()
        {
            Console.WriteLine(nameof(TestOrderByWithComparer));
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
                .OrderBy(s => s.Name, new MyComparer());
            resStud.ToList().ForEach(Console.WriteLine);
            //no version for Query expression
            Console.WriteLine("--------");
        }

        public static void TestTakeAndSkip()
        {
            Console.WriteLine(nameof(TestTakeAndSkip));
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
               .Skip(4).Take(5);
            resStud.ToList().ForEach(Console.WriteLine);
            //no version for Query expression
            Console.WriteLine("--------");
        }

        public static void TestTakeWhileAndSkipWhile()
        {
            Console.WriteLine(nameof(TestTakeWhileAndSkipWhile));
            Generator.GenerateStudentsWithTopicsEasy().ToList().ForEach(Console.WriteLine);
            Console.WriteLine("------------");
            var resStud = Generator.GenerateStudentsWithTopicsEasy()
               .SkipWhile(s => s.Active)
               .SkipWhile(s => !s.Active)
               .TakeWhile(s => s.Active);
            resStud.ToList().ForEach(Console.WriteLine);
            //no version for Query expression
            Console.WriteLine("--------");
        }

        public static void TestLazyExecution()
        {
            Console.WriteLine(nameof(TestLazyExecution));
            var studs = Generator.GenerateStudentsWithTopicsEasy();
            var resStud = from s in studs
                          where s.Index < 20000 && s.Name.Length <= 6
                          select s;

            studs.Add(new StudentWithTopics(30, 15000, "Wuc", Gender.Male, true, 1,
                    ["C#", "Java", "algorithms"]));

            resStud.ToList().ForEach(Console.WriteLine);

            Console.WriteLine("---------------");
            var resStud2 = (from s in studs
                            where s.Index < 20000 && s.Name.Length <= 6
                            select s).Count();

            studs.Add(new StudentWithTopics(31, 15001, "Wow", Gender.Female, true, 1,
                    ["C#"]));

            Console.WriteLine(resStud2);
            Console.WriteLine("--------");

        }

        public static void TestToDictionaryAndToLookup()
        {
            Console.WriteLine(nameof(TestToDictionaryAndToLookup));
            var resStud = Generator.GenerateStudentsWithTopicsEasy().ToDictionary(s => s.Index, s => s.Name);
            resStud.ToList().ForEach(s => Console.WriteLine(s.Key + "-->" + s.Value));

            Console.WriteLine("---------------");
            var resStud2 = Generator.GenerateStudentsWithTopicsEasy().ToLookup(s => s.Name);
            foreach (var dept in resStud2)
            {
                Console.WriteLine(dept.Key);
                resStud2[dept.Key].ToList().ForEach(s => Console.WriteLine("  " + s));
            }
            Console.WriteLine("--------");
        }

        public static void TestGroupBy()
        {
            Console.WriteLine(nameof(TestGroupBy));
            var resStud = from s in Generator.GenerateStudentsWithTopicsEasy()
                          group s by s.DepartmentId;

            foreach (var dept in resStud)
            {
                Console.WriteLine(dept.Key);
                dept.ToList().ForEach(s => Console.WriteLine("  " + s));
            }
            Console.WriteLine("--------");
        }


        public static void TestGroupByComplex()
        {
            Console.WriteLine(nameof(TestGroupByComplex));
            var resStud = from s in Generator.GenerateStudentsWithTopicsEasy()
                          group s by new { s.Active, s.Gender } into sGroup
                          orderby sGroup.Key.Active, sGroup.Key.Gender
                          select new
                          {
                              Active = sGroup.Key.Active,
                              sGroup.Key.Gender, // simpler
                              Students = sGroup.OrderBy(s => s.Name)
                          };

            foreach (var group in resStud)
            {
                Console.WriteLine((group.Active ? "active" : "no active") + "      " + group.Gender);
                group.Students.ToList().ForEach(s => Console.WriteLine("  " + s));
            }
            Console.WriteLine("--------");
        }

        public static void TestGroupJoin()
        {
            Console.WriteLine(nameof(TestGroupJoin));
            var resStud = Generator.GenerateDepartmentsEasy()
                .GroupJoin(Generator.GenerateStudentsWithTopicsEasy(),
                        dept => dept.Id,
                        stud => stud.DepartmentId,
                        (department, students) => new
                        {
                            Department = department,
                            Students = students
                        }
                        );

            foreach (var group in resStud)
            {
                Console.WriteLine(group.Department.Name);
                group.Students.ToList().ForEach(s => Console.WriteLine("  " + s));
            }
            Console.WriteLine("------------");
            var resStud2 = from d in Generator.GenerateDepartmentsEasy()
                           join s in Generator.GenerateStudentsWithTopicsEasy()
                           on d.Id equals s.DepartmentId into dGroup
                           select new
                           {
                               Department = d,
                               Students = dGroup
                           };


            foreach (var group in resStud2)
            {
                Console.WriteLine(group.Department.Name);
                group.Students.ToList().ForEach(s => Console.WriteLine("  " + s));
            }

            Console.WriteLine("--------");
        }
        public static void TestJoinSpecial()
        {
            Console.WriteLine(nameof(TestJoinSpecial));
            var resStud = Generator.GenerateDepartmentsEasy()
                        .Join(Generator.GenerateStudentsWithTopicsEasy(),
                        dept => dept.Id,
                        stud => stud.DepartmentId,
                        (department, student) => new
                        {
                            DepartmentName = department.Name,
                            StudentName = student.Name
                        }
                        );

            foreach (var elem in resStud)
            {
                Console.WriteLine($"{elem.DepartmentName} -> {elem.StudentName}");
            }
            //Console.WriteLine("------------");
            //var resStud2 = from d in Generator.GenerateDepartmentsEasy()
            //               join s in Generator.GenerateStudentsEasy()
            //               on d.Id equals s.DepartmentId into dGroup
            //               select new
            //               {
            //                   Department = d,
            //                   Students = dGroup
            //               };


            //foreach (var group in resStud2)
            //{
            //    Console.WriteLine(group.Department.Name);
            //    group.Students.ToList().ForEach(s => Console.WriteLine("  " + s));
            //}

            Console.WriteLine("--------");
        }


        public static void TestJoin()
        {
            Console.WriteLine(nameof(TestJoin));
            var studs = Generator.GenerateStudentsWithTopicsEasy();
            // there are no 6 department
            studs.Add(new StudentWithTopics(30, 15000, "Wuc", Gender.Male, true, 6,
                            ["C#", "Java", "algorithms"]));
            var resStud = studs
                         .Join(Generator.GenerateDepartmentsEasy(),
                        stud => stud.DepartmentId,
                        dept => dept.Id,
                        (student, department) => new
                        {
                            DepartmentName = department.Name,
                            StudentName = student.Name
                        }
                        );

            foreach (var elem in resStud)
            {
                Console.WriteLine($"{elem.DepartmentName} -> {elem.StudentName}");
            }
            Console.WriteLine("------------");
            var resStud2 = from s in Generator.GenerateStudentsWithTopicsEasy()
                           join d in Generator.GenerateDepartmentsEasy()
                           on s.DepartmentId equals d.Id
                           select new
                           {
                               DepartmentName = d.Name,
                               StudentName = s.Name
                           };
            foreach (var elem in resStud2)
            {
                Console.WriteLine($"{elem.DepartmentName} -> {elem.StudentName}");
            }
            Console.WriteLine("--------");
        }

        public static void CartesianProduct()
        {
            Console.WriteLine(nameof(CartesianProduct));
            var resCart = from num in Generator.GenerateIntsEasy()
                          where num % 2 == 0
                          from d in Generator.GenerateNamesEasy()
                          where d.Length < 7
                          select new
                          {
                              Number = num,
                              Word = d
                          };
            foreach (var elem in resCart)
            {
                Console.WriteLine($"{elem.Number} -> {elem.Word}");
            }
            Console.WriteLine("--------");
            var resCart2 = Generator.GenerateIntsEasy()
                           .Where(num => num % 2 == 0)
                           .SelectMany(
                                s => Generator.GenerateNamesEasy().Where(s => s.Length < 7),
                                (n, s) => new {
                                    Number = n,
                                    Word = s
                                });
            foreach (var elem in resCart2)
            {
                //                Console.WriteLine($"{elem.Number} -> {elem.Word}");
                Console.WriteLine($"{elem}");
            }
            Console.WriteLine("--------");
        }

        class Comp : IEqualityComparer<StudentWithTopics>
        {
            public bool Equals(StudentWithTopics x, StudentWithTopics y)
            {
                return x.Id == x.Id;
            }

            public int GetHashCode(StudentWithTopics obj)
            {
                return obj.Id.GetHashCode();
            }
        }
        public static void TestDistinc()
        {
            Console.WriteLine(nameof(TestDistinc));
            var set1 = Generator.GenerateStudentsWithTopicsEasy()
                       .Where(s => s.Id >= 0 && s.Id <= 2)
                       .ToList();
            set1.Add(new StudentWithTopics(1, 12345, "Nowak", Gender.Female, true, 1,
                    ["C#", "PHP", "algorithms"])); // copy od first student

            set1.Distinct().ToList().ForEach(Console.WriteLine);
            Console.WriteLine("----------------");
            set1.Distinct(new Comp()).ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }

        public static void TestUnion()
        {
            Console.WriteLine(nameof(TestUnion));
            var set1 = Generator.GenerateStudentsWithTopicsEasy()
                       .Where(s => s.Id >= 1 && s.Id <= 4);
            var set2 = Generator.GenerateStudentsWithTopicsEasy()
                       .Where(s => s.Id >= 3 && s.Id <= 6);
            set1.Union(set2).ToList().ForEach(Console.WriteLine);
            Console.WriteLine("----------------");
            set1.Union(set2, new Comp()).ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }

        public static void TestUnionAnnonymous()
        {
            Console.WriteLine(nameof(TestUnionAnnonymous));
            var set1 = Generator.GenerateStudentsWithTopicsEasy()
                       .Where(s => s.Id >= 1 && s.Id <= 4)
                       .Select(s => new
                       {
                           s.Id,
                           s.Index,
                           s.Name
                       }
                       );
            var set2 = Generator.GenerateStudentsWithTopicsEasy()
                       .Where(s => s.Id >= 3 && s.Id <= 6)
                       .Select(s => new
                       {
                           s.Id,
                           s.Index,
                           s.Name
                       }
                       );
            set1.Union(set2).ToList().ForEach(Console.WriteLine);
            Console.WriteLine("--------");
        }

        static void TestClauseWithoutLet()
        {
            Console.WriteLine(nameof(TestClauseWithoutLet));
            ClauseWithoutLet(".", "*");
        }
        static void ClauseWithoutLet(string rootDirectory, string searchPattern)
        {
            Console.WriteLine(nameof(ClauseWithoutLet));
            IEnumerable<string> filenames = Directory.GetFiles(rootDirectory, searchPattern);
            var fileResults =
              from fileName in filenames
              orderby new FileInfo(fileName).Length, fileName
              select new FileInfo(fileName);
            foreach (var fileResult in fileResults)
            {
                Console.WriteLine($"{fileResult.Name} ({fileResult.Length})");
            }
            Console.WriteLine("--------");
        }


        static void Main()
        {
            ShowAllCollections();
            Console.WriteLine("--------------");
            ClauseWhereSimple();
            SimpleAggregiates();
            ComplexAggregiates();
            WhereWithPos();
            TestSelect();
            TestSelectMany();
            TestSelectManyQuery();
            TestSelectManyWith2Lambdas();
            TestOrderBy();
            TestOrderByWithComparer();
            TestTakeAndSkip();
            TestTakeWhileAndSkipWhile();
            TestLazyExecution();
            TestToDictionaryAndToLookup(); // ????
            TestGroupBy();
            TestGroupByComplex();
            TestGroupJoin();
            TestJoinSpecial();  // ???
            TestJoin();
            CartesianProduct();
            TestDistinc();
            TestUnion();
            TestUnionAnnonymous();
            TestClauseWithoutLet();
        }
    }
}
