using System.ComponentModel.DataAnnotations;

namespace lab10.Models
{
    public class Category
    {
        public int Id { get; set; }
        [Required]
        [MaxLength(100)]
        public string? Name { get; set; }

        public ICollection<Article> Articles { get; set; } = [];
    }
}
