using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;
namespace lab12.Models
{
    public class Category
    {
        public int Id { get; set; }
        [Required]
        [MaxLength(100)]
        public string? Name { get; set; }

        [JsonIgnore]
        public ICollection<Article> Articles { get; set; } = [];
    }
}
