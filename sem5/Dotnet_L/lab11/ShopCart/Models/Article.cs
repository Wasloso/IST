using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace lab10.Models
{
    public class Article
    {
        public int Id { get; set; }
        [Required]
        [MaxLength(100)]
        public required string Title { get; set; }
        [Required]
        [DisplayFormat(DataFormatString = "{0:C}")]
        public Decimal Price { get; set; }
        [NotMapped]
        public IFormFile? ImageFile { get; set; }
        [DisplayName("Image")]
        public string? ImagePath { get; set; }

        [ForeignKey("Category")]
        [DisplayName("Category")]
        public int CategoryId { get; set; }
        public Category? Category { get; set; }

    }
}
